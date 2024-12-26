@echo off
echo ===================================
echo Verificando conexoes remotas ativas...
echo ===================================
qwinsta
echo.
echo ===================================
echo Desconectando sessoes remotas...
echo ===================================
for /f "skip=1 tokens=2" %%i in ('qwinsta ^| find /i "rdp-tcp"') do logoff %%i
echo.
echo Conexoes remotas foram encerradas.
echo ===================================
pause
