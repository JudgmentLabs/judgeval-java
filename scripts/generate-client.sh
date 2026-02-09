#!/bin/bash

python3 scripts/generate_client.py "${1:-http://localhost:10001/openapi/json}"
