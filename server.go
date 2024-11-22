package main

import (
	"log"
	"net/http"
	"os"

	"github.com/brreg/create-branch-backend/api"
	"github.com/dotenv-org/godotenvvault"
	"github.com/gorilla/handlers"
	"github.com/gorilla/mux"
)

func main() {
	err := godotenvvault.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	router := mux.NewRouter()

	router.HandleFunc("/api/qr-code", api.GetQrCodeUrl).Methods(http.MethodGet)
	headersOk := handlers.AllowedHeaders([]string{"x-session-id"})
	originsOk := handlers.AllowedOrigins([]string{"*"})
	methodsOk := handlers.AllowedMethods([]string{"GET", "HEAD", "POST", "PUT", "OPTIONS"})

	log.Fatal(http.ListenAndServe(":"+os.Getenv("PORT"), handlers.CORS(originsOk, headersOk, methodsOk)(router)))
}
