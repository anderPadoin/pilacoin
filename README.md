# PilaCoin
Processos:

1) Geração de par de chaves pública e privada, com armazenamento em disco.

2) Mineração de PilaCoin:
    - Instanciar a classe PilaCoin e atribuir valores aos atributos: dataCriacao, idCriador, chaveCriador (ch. pública do criador);
    - Dados os valores anteriores atribuir um número mágico que torne o hash (SHA-256) do objeto serializado, um número
    qualquer menor do que 99999998000000000000000000000000000000000000000000000000000000000000000.
    - Encontrado o número mágico deverá ser enviado ao master para cadastro e assinatura (ver processo 4);

3) Descoberta do Master:
    - Enviar uma mensagem do tipo DISCOVER em broadcast na rede interna (Datagrama UDP) pela porta 3333.
    - Aguardar uma mensagem DISCOVER_RESPONSE na porta 3333 onde master seja = true.
    - Conferir a assinatura digital da mensagem (chave pública do servidor deverá estar armazenada em disco).
    - Armazenar o endereço IP e porta do servidor para comunicações seguras.
    - enviar mensagens do tipo DISCOVER a cada 15s;

4) Validação de um PilaCoin recém criado:
    - Após descoberto o master abrir um socket no endereço e porta descobertos;
    - Criar uma chave de sessão e criptografá-la com a chave pública do servidor;
    - Serializar o pila e criptografá-lo com a chave de sessão.
    - Colocar o pila e a chave de sessão em um ObjetoTroca e enviá-lo ao servidor para validação.
    - Receber outro objeto troca com o pila já validado e criptografado com a mesma chave de sessão.
    - Armazená-lo em meio persistente para uso futuro.

5) Descoberta de outros usuários:
    - Aguardar mensagens do tipo DISCOVER e cadastrar internamente: idUsuario, Chave Pública, endereço IP e Porta;

6) Transferência de PilaCoin:
    - Pegar um PilaCoin que é seu e criar uma nova transação, indicando a data e o id do novo dono.
    - Assinar a transação com a sua chave privada.
    - Adicionar a nova transação ao PilaCoin, e divulgá-lo como uma mensagem do tipo PILA_TRANSF em broadcast.

