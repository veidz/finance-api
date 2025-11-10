#!/bin/bash
SERVICE=${1:-app}

echo "📋 Viewing logs for: $SERVICE"
echo "   Available services: app, postgres, pgadmin"
echo "   Usage: ./scripts/logs-dev.sh [service_name]"
echo ""

docker-compose -f docker-compose.dev.yml logs -f $SERVICE
