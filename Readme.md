# Monitoramento de Uso da CPU via TCP

Este projeto foi desenvolvido como trabalho da disciplina de Sistemas Distribuídos do curso de Sistemas de Informação. O objetivo do projeto é criar um sistema cliente-servidor que monitora o uso da CPU do servidor e envia os dados para o cliente via TCP.

## Descrição

O projeto consiste em dois programas principais:

1. **TcpServidor.java**: Um servidor TCP que coleta dados de uso da CPU e os envia para o cliente.
2. **TcpCliente.java**: Um cliente TCP que se conecta ao servidor, solicita os dados de uso da CPU e os exibe.

## Funcionalidades

- O servidor coleta dados de uso da CPU a cada segundo durante 10 segundos.
- O servidor envia os dados coletados para o cliente, incluindo a data, hora e uso da CPU.
- O servidor calcula e envia o valor médio de uso da CPU e o tempo de processamento.
- O cliente exibe os dados recebidos do servidor.

## Estrutura do Projeto
  ├── TcpServidor.java
  ├── TcpCliente.java 
  └── README.md
  
## Como Executar

### Pré-requisitos

- Java Development Kit (JDK) instalado.

### Passos para Compilar e Executar

1. **Compile o Servidor e o Cliente:**

   ```sh
   javac TcpServidor.java TcpCliente.java
   ```

2. **Inicie o Servidor:**

   ```sh
   java TcpServidor
   ```
   O servidor ficará aguardando conexões na porta 7896.

3. **Inicie o Cliente:**

   ```sh
   java TcpCliente cpu <endereço IP do servidor>
   ```
   Substitua `<endereço IP do servidor>` pelo endereço IP do servidor onde o TcpServidor está em execução.

### Exemplo de Uso

Mensagens de Ajuda
Se o parâmetro fornecido ao cliente for `-help`, o cliente exibirá uma mensagem de ajuda:

## Autor

Isaac Machado