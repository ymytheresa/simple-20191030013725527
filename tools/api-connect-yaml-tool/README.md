IBM API Connect YAML Tool
=====

Convert swagger-ui json file to IBM API Connect yaml file.

Please check documents in index.js for more details.

## How to run

Node.js 6.x and above is required.
This tool will grab json from here: `https://bocapis.mybluemix.net/v2/api-docs?group=xxx`, and automatically generate yml file required by IBM Api Connect.

Issue the following commands in your terminal.

```sh
npm install
node index.js
```

The generated *.yaml file will be in `output` directory.

## Sample log

```
https://bocapis.mybluemix.net/v2/api-docs?group=Account Operations
https://bocapis.mybluemix.net/v2/api-docs?group=Appointments
https://bocapis.mybluemix.net/v2/api-docs?group=Bank Information
https://bocapis.mybluemix.net/v2/api-docs?group=Card Operations
https://bocapis.mybluemix.net/v2/api-docs?group=Customer Operations
https://bocapis.mybluemix.net/v2/api-docs?group=Internal
https://bocapis.mybluemix.net/v2/api-docs?group=Investments
https://bocapis.mybluemix.net/v2/api-docs?group=Market Information
https://bocapis.mybluemix.net/v2/api-docs?group=Personal Loans
POST /internal/reset-all-db
POST /internal/reset-db
output: /Users/cyper/gitibm/BoCAPIs/tools/api-connect-yaml-tool/output/internal.yaml

GET /api/credit-cards
POST /api/credit-cards/{card_no}/payment
GET /api/credit-cards/{card_no}/transactions
output: /Users/cyper/gitibm/BoCAPIs/tools/api-connect-yaml-tool/output/card-operations.yaml

GET /api/bank-info/atms
GET /api/bank-info/branches
output: /Users/cyper/gitibm/BoCAPIs/tools/api-connect-yaml-tool/output/bank-information.yaml

GET /api/customer/profile
output: /Users/cyper/gitibm/BoCAPIs/tools/api-connect-yaml-tool/output/customer-operations.yaml

GET /api/services/appointments/{phone_no}
POST /api/services/appointments/{phone_no}
DELETE /api/services/appointments/{phone_no}/{appoinement_id}
output: /Users/cyper/gitibm/BoCAPIs/tools/api-connect-yaml-tool/output/appointments.yaml

GET /api/investments
POST /api/investments/stock
output: /Users/cyper/gitibm/BoCAPIs/tools/api-connect-yaml-tool/output/investments.yaml

GET /api/market-info/fx-rate
GET /api/market-info/stock
output: /Users/cyper/gitibm/BoCAPIs/tools/api-connect-yaml-tool/output/market-information.yaml

GET /api/accounts
GET /api/accounts/third-party-name
POST /api/accounts/{account_no}/forex
POST /api/accounts/{account_no}/money-transfer
POST /api/accounts/{account_no}/money-transfer-fps
GET /api/accounts/{account_no}/transactions
output: /Users/cyper/gitibm/BoCAPIs/tools/api-connect-yaml-tool/output/account-operations.yaml

GET /api/personal-loans
POST /api/personal-loans/applications
POST /api/personal-loans/personalized-rate
output: /Users/cyper/gitibm/BoCAPIs/tools/api-connect-yaml-tool/output/personal-loans.yaml

```