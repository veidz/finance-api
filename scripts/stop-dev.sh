#!/bin/bash
echo "🛑 Stopping Finance API development environment..."

docker-compose -f docker-compose.dev.yml down

echo "✅ Development environment stopped!"
echo ""
echo "💡 Tip: Data is persisted in Docker volumes"
echo "   View volumes: docker volume ls | grep finance-api"
echo "   Remove data: docker-compose -f docker-compose.dev.yml down -v"
