#!/bin/bash

echo "═══════════════════════════════════════════════════"
echo "  APPLICATION BANCAIRE - LANCEMENT                 "
echo "═══════════════════════════════════════════════════"
echo ""
echo "🔐 Démarrage de l'application avec interface de connexion..."
echo ""

./mvnw clean javafx:run -Djavafx.mainClass="com.example.cbclient.BanqueLoginApp"
