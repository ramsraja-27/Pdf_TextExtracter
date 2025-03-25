PDF Reader API using LLM

Overview

A Spring Boot-based REST API that extracts financial details from CASA bank statements (PDFs) using GooseAI.

Tech Stack

1.Java 17, Spring Boot, Maven
2.GooseAI (GPT models for text processing)
3.Apache PDFBox (PDF text extraction)

API Endpoint
Upload PDF for Extraction

POST /api/pdf/extract
Request: multipart/form-data with file (PDF)
Expected Response:
{
"name": "John Doe",
"email": "johndoe@example.com",
"opening_balance": "$5,000",
"closing_balance": "$4,200"
}


