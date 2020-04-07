@echo off
ECHO Compilando...
javac -cp ".;weka.jar" Entrenador.java

CLS
ECHO ejecutando
java --add-opens=java.base/java.lang=ALL-UNNAMED -cp ".;weka.jar" Entrenador
pause
exit