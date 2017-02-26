 
# Problema da malha logística
 
 Webservice que calcula a rota mais curta em um grafo.
 
## Tecnologias utilizadas
 
 * Linguagem Java: Das 3 linguagens disponíveis para a solução do problema, Java é a que possuo mais familiaridade.
 * Eclipse IDE: Ambiente de desenvolvimento que já vem com várias ferramentas inclusas no pacote para não se perder tanto tempo com build e deployment. Também é uma das IDEs mais utilizadas no mundo.
 * JAX-RC & Jersey: usados para montar um Webservice RESTful. Essas tecnlogias foram escolhidas pela facilidade de serem criadas com Eclipse IDE
 * Jackson: converte objetos Java em JSON, e vice-versa. Usado para montar as respostas das requisições do webservice.
 * Maven: Responsável por baixar as dependências.
 * Tomcat maven plugin: Usado para montar o webservice localmente. Com isso, não é necessário baixar um servidor do Apache Tomcat, basta apenas colocar **tomcat7:run** no **goal** do build maven.
 * MongoDB: Banco de dados não relacional. Escolhido pela simplicidade de seu uso e também por se encaixar na solução do problema. A solução não precisará mais do que uma tabela.
 
## Algoritmo para resolução da malha
 
 O algoritmo escolhido foi Floy-Warshall. Esse algoritmo possui um tempo de execução O(V³), sendo V o número total de vértices no grafo. Porém esse tempo ocorre somente uma vez na construção da matriz de resposta.
 Diferente, por exemplo, de algoritmos como Dijkstra em que o tempo de execução é de  O(V log V + E log V) no pior caso para toda chamada de seu algoritmo, Floyd-Warshall consome O(1) para as chamadas consecutivas à primeira. Isso partindo do presuposto de que os mapas de distância não irão ser atualizados com muita frequência (afinal custa caro pro governo criar novas rotas).
 
## Exemplos de dormatos na requisição e na resposta do Webservice
 É aconselhável utilizar ferramentas para fazer requisições como Postman para Chrome e Poster para Firefox
 
 * POST: 
  * Na URL de requisição coloque: http://localhost:8080/malhalogistica/webservice/mapa/
  * Postman: escolha a aba *Body* -> escolha opção *raw* -> Mude *Text* para *JSON(aaplication/json)* 
  * Poster: digite *application/json* no campo *Content Type*
  * Coloque um JSON que siga o seguinte formato: {"nome":"SP","matrizInicialDeDistancia":{"[A, B]":10,"[A, C]":20,"[B, D]":15,"[C, D]":30,"[B, E]":50,"[D, E]":30}}
  * Clique no botão SEND caso esteja usando Postman e no botão POST caso esteja usando Poster
 
 * GET: 
  * para obter a matriz de adjacencia: http://localhost:8080/malhalogistica/webservice/mapa/{nome_do_mapa}
 
## Como compilar e subir o Webservice
 
 Na IDE do Eclipse, clique com o botão direito em cima do projeto, vá em: Run As... -> Maven build... -> Digite em goals **clean package generate-sources install tomcat7:run** -> clique no botão Run.
