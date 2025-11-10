#!/bin/bash
echo "🚀 Starting Finance API development environment..."

if ! docker info > /dev/null 2>&1; then
  echo "❌ Docker is not running. Please start Docker first."
  exit 1
fi

if [ ! -f .env ]; then
  echo "⚠️  .env file not found. Creating from .env.example..."
  cp .env.example .env
  echo "✅ .env file created. Please update it with your credentials."
fi

echo "📦 Starting all services (PostgreSQL + Finance API)..."
docker-compose -f docker-compose.dev.yml up -d

echo ""
echo "📋 Following logs (Ctrl+C to stop viewing logs)..."
echo ""
docker-compose -f docker-compose.dev.yml logs -f app
