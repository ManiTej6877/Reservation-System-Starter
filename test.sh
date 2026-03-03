#!/bin/bash

# Script to run all tests for the Flight Reservation System

echo "Running all tests..."
./mvnw test

# Capture exit code
EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo ""
    echo "All tests passed successfully!"
else
    echo ""
    echo "Some tests failed. Please check the output above."
fi

exit $EXIT_CODE
