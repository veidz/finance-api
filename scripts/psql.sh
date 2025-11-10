#!/bin/bash
echo "🐘 Connecting to PostgreSQL..."
echo ""

docker-compose -f docker-compose.dev.yml exec postgres psql -U postgres -d financeapi_dev
