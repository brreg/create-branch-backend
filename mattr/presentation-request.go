package mattr

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
)

type PresentationRequest struct {
	Challenge   string `json:"challenge"`
	TemplateID  string `json:"templateId"`
	DID         string `json:"did"`
	CallbackURL string `json:"callbackUrl"`
}

type PresentationResponse struct {
	DidcommURI string `json:"didcommUri"`
}

func CreatePresentationUrl(userSessionId string) (*PresentationResponse, error) {
	mattrURL := os.Getenv("MATTR_TENANT_URL") + "/v2/credentials/web-semantic/presentations/requests"
	accessToken, err := fetchAccessToken()
	if err != nil {
		return nil, err
	}

	var callbackUrl string
	if os.Getenv("ENV") == "dev" {
		callbackUrl = "https://webhook.site/1b3c5c4c-4270-4352-97c3-a6725e6fde23"
	} else {
		callbackUrl = "https://webhook.site/1b3c5c4c-4270-4352-97c3-a6725e6fde23"
	}

	presentationBody := PresentationRequest{
		Challenge:   userSessionId,
		TemplateID:  "97efa3c9-0c93-4619-9d43-6725304143ec",
		DID:         "did:key:z6MktQLEQ9Lze3s3eUKwtxVXDZSPFyyXcxxNVHXFN5ueaEbq",
		CallbackURL: callbackUrl,
	}

	payload, _ := json.Marshal(presentationBody)
	req, _ := http.NewRequest("POST", mattrURL, bytes.NewBuffer(payload))
	req.Header.Set("Authorization", "Bearer "+*accessToken)
	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusCreated {
		bodyBytes, _ := io.ReadAll(resp.Body)
		return nil, fmt.Errorf("non-200 response: %s", string(bodyBytes))
	}

	var presentationResponse PresentationResponse
	err = json.NewDecoder(resp.Body).Decode(&presentationResponse)
	if err != nil {
		return nil, err
	}

	return &presentationResponse, nil
}
