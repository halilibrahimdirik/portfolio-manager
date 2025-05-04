# Portfolio Manager Backend Service

## Fund Price Updates

To manually update fund prices in the database, use the following command:

```bash
curl -X POST http://localhost:8080/api/fund-prices/crawl