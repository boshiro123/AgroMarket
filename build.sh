#!/bin/bash

# Функция для проверки наличия команды
check_command() {
    if ! command -v $1 &> /dev/null; then
        echo "❌ $1 не установлен"
        return 1
    else
        echo "✅ $1 уже установлен"
        return 0
    fi
}

# Функция для установки Docker на macOS
install_docker_mac() {
    echo "📦 Установка Docker через Homebrew..."
    brew install --cask docker
    
    # Запуск Docker.app
    open /Applications/Docker.app
    
    echo "⏳ Ожидание запуска Docker..."
    while ! docker system info &>/dev/null; do
        sleep 1
    done
}

# Функция для установки Docker Compose на macOS
install_docker_compose_mac() {
    echo "📦 Установка Docker Compose через Homebrew..."
    brew install docker-compose
}

# Проверка наличия Homebrew (для macOS)
if [[ "$OSTYPE" == "darwin"* ]]; then
    if ! command -v brew &> /dev/null; then
        echo "🍺 Установка Homebrew..."
        /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    fi
fi

# Проверка и установка Docker
if ! check_command "docker"; then
    if [[ "$OSTYPE" == "darwin"* ]]; then
        install_docker_mac
    else
        echo "❌ Пожалуйста, установите Docker вручную для вашей операционной системы"
        exit 1
    fi
fi

# Проверка и установка Docker Compose
if ! check_command "docker-compose"; then
    if [[ "$OSTYPE" == "darwin"* ]]; then
        install_docker_compose_mac
    else
        echo "❌ Пожалуйста, установите Docker Compose вручную для вашей операционной системы"
        exit 1
    fi
fi

# Запуск сборки и развертывания
echo "🚀 Запуск сборки проекта..."
docker compose down
docker compose build
docker compose up -d

echo "✨ Готово! Проект успешно собран и запущен" 