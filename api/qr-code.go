package api

import (
	"encoding/json"
	"net/http"

	"github.com/brreg/create-branch-backend/mattr"
)

func GetQrCodeUrl(w http.ResponseWriter, r *http.Request) {
	userSessionId := r.Header.Get("x-session-id")
	if len(userSessionId) == 0 {
		http.Error(w, "missing x-session-id in header", http.StatusBadRequest)
		return
	}

	qrCodeUrl, err := mattr.CreatePresentationUrl(userSessionId)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(qrCodeUrl)
}
