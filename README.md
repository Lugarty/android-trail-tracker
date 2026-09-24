# Android Trail Tracker

Aplicação Android em **Java** para registrar, armazenar e visualizar trilhas geográficas usando GPS, Google Maps e SQLite.

O projeto combina coleta de localização em tempo real com persistência local estruturada em duas entidades: a trilha e os pontos geográficos pertencentes a ela.

## O que este projeto demonstra

- desenvolvimento Android com Java;
- integração com **Google Maps SDK**;
- uso do **Fused Location Provider**;
- atualização periódica de localização;
- representação de coordenadas com `LatLng`;
- persistência local com **SQLite**;
- relacionamento entre trilhas e pontos geográficos;
- operações CRUD;
- navegação entre Activities;
- configuração de mapa normal/satélite;
- separação entre camada de interface e acesso a dados.

## Fluxo principal

```text
GPS / Fused Location Provider
           |
           v
RegistrarTrilhaActivity
           |
           +--> GoogleMap / LatLng
           |
           +--> métricas da trilha
           |
           v
TrilhaDBHelper
     |             |
     v             v
  trilha      ponto_trilha
     |             |
     +---- 1:N ----+
```

## Funcionalidades

### Registro de trilha

Durante o registro, a aplicação recebe atualizações de localização e usa os pontos para acompanhar o percurso.

### Visualização em mapa

Os pontos salvos podem ser reconstruídos e exibidos posteriormente no Google Maps.

### Métricas

A estrutura da trilha armazena informações como:

- início e fim;
- velocidade máxima;
- velocidade média;
- distância total.

### Gerenciamento

É possível:

- listar trilhas salvas;
- alterar o nome;
- excluir uma trilha;
- excluir todos os registros;
- abrir uma trilha específica para visualização.

## Persistência

A classe `TrilhaDBHelper` utiliza `SQLiteOpenHelper` e cria duas tabelas.

### `trilha`

Armazena os dados gerais do percurso.

Campos principais:

- `id`;
- `nome`;
- `data_inicio`;
- `data_fim`;
- `velocidade_maxima`;
- `velocidade_media`;
- `distancia_total`.

### `ponto_trilha`

Armazena cada ponto coletado durante o percurso.

Campos principais:

- `id`;
- `trilha_id`;
- `latitude`;
- `longitude`;
- `timestamp`.

O banco modela um relacionamento **1:N** entre trilha e pontos por meio de `trilha_id`.

## Estrutura principal

```text
app/src/main/java/com/example/atividaden2/
├── MainActivity.java
├── activities/
│   ├── ConfigActivity.java
│   ├── ConsultarTrilhasActivity.java
│   ├── RegistrarTrilhaActivity.java
│   └── VisualizarTrilhaActivity.java
└── database/
    ├── Trilha.java
    ├── PontoTrilha.java
    └── TrilhaDBHelper.java
```

## Tecnologias

| Área | Tecnologia |
|---|---|
| Linguagem | Java |
| Plataforma | Android |
| Mapas | Google Maps SDK |
| Localização | Fused Location Provider |
| Persistência | SQLite |
| UI | Android Views / Material |
| Build | Gradle |

## Segurança da API

A chave do Google Maps **não está versionada** no repositório.

No `AndroidManifest.xml` existe apenas o placeholder:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="SUA_CHAVE_AQUI" />
```

Para executar o projeto, gere uma chave no Google Cloud Console e substitua o placeholder localmente.

## Como executar

1. Clone o repositório:

```bash
git clone https://github.com/Lugarty/android-trail-tracker.git
```

2. Abra o projeto no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Habilite o Maps SDK for Android em um projeto do Google Cloud.
5. Configure sua chave no `AndroidManifest.xml`.
6. Execute em dispositivo físico ou emulador com Google Play Services.

## Permissões

A aplicação solicita:

- `ACCESS_FINE_LOCATION`;
- `ACCESS_COARSE_LOCATION`.

O comportamento completo de rastreamento depende da autorização de localização concedida pelo usuário.

## Decisões técnicas

### SQLite local

O projeto mantém os dados offline e dispensa backend para armazenar o histórico de trilhas.

### Separação entre trilha e pontos

Em vez de armazenar o percurso como um único campo, cada ponto geográfico possui sua própria linha associada à trilha. Isso simplifica a reconstrução da rota no mapa.

### Fused Location Provider

A coleta usa a API de localização do Google Play Services, que abstrai múltiplas fontes de localização do dispositivo.

## Limitações atuais

- banco local na versão 1; `onUpgrade` recria as tabelas em vez de realizar migração incremental;
- não há sincronização em nuvem;
- não há autenticação de usuário;
- a chave do Google Maps precisa ser configurada manualmente;
- não há suíte de testes automatizados no repositório.

## Próximos passos

- adicionar migrações de banco sem perda de dados;
- criar testes para regras de persistência e cálculo;
- exportar trilhas em formato interoperável, como GPX;
- melhorar tratamento de permissões e estados de GPS;
- adicionar camada de repositório para desacoplar Activities do SQLite.

## Autor

**Anísio Oliveira Albuquerque Filho**  
GitHub: [@Lugarty](https://github.com/Lugarty)
