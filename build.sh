#!/bin/bash
# Build and run script for the web scraper

echo "Compiling scraper.java..."
javac -cp jsoup-1.21.2.jar scraper.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Running scraper..."
    java -cp .:jsoup-1.21.2.jar scraper
else
    echo "Compilation failed!"
    exit 1
fi


