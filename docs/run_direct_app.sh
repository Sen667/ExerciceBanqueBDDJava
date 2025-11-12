#!/bin/bash

echo "═══════════════════════════════════════════════════"
echo "  APPLICATION BANCAIRE - MODE DIRECT                "
echo "═══════════════════════════════════════════════════"
echo ""
echo "⚡ Démarrage de l'application sans connexion..."
echo ""

./mvnw clean javafx:run -Djavafx.mainClass="com.example.cbclient.BanqueApp"
