#!/bin/bash
echo "🔄 Rebuilding Finance API development environment..."

docker-compose -f docker-compose.dev.yml down

echo "🏗️  Building images..."
docker-compose -f docker-compose.dev.yml build --no-cache

echo "🚀 Starting services..."
docker-compose -f docker-compose.dev.yml up -d

echo ""
echo "📋 Following logs..."
docker-compose -f docker-compose.dev.yml logs -f app
