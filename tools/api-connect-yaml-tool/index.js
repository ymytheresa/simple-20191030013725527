/**
 * IBM API Connect YAML Tool
 * =====
 *
 * Convert swagger-ui json file to IBM API Connect yaml file.
 *
 * The following features will be added by this tool:
 *
 * 1. x-ibm-name
 * 2. x-ibm-configuration: assembly
 * 3. securityDefinitions: oauth
 * 4. security: oauth
 * 5. schemes: https
 *
 * Please refer to ./data/template/api-connect.yaml for more details, this tool will use that template file as input.
 *
 * @author cyper <yinsb@cn.ibm.com>
 *
 */
const jsyaml = require('js-yaml'),
  fs = require('fs'),
  path = require('path'),
  request = require('request');

const FILE_ENCODING = 'utf8';
//const VERSION = '2018';
const SERVER_URL = 'https://bocapis.mybluemix.net';
const groupsOfApplication = ['Bank Information', 'Market Information', 'Appointments'];

const IBM_TEMPLATE_FILE = path.resolve(__dirname, './data/template/api-connect.yaml');

const outputDir = path.resolve(__dirname, './output');

// create output dir
if (!fs.existsSync(outputDir)) {
  fs.mkdirSync(outputDir);
}

// 从swagger-ui上抓取api definition.
request(SERVER_URL + '/swagger-resources', {json: true}, function (err, res, data) {
  if (err) {
    return console.log(err);
  }

  let groups = data;
  groups.forEach(group => {
    let groupName = group.name;
    let apiDocsUrl = SERVER_URL + group.location;

    console.log(apiDocsUrl);

    request(apiDocsUrl, {json: true}, function (err, res, data) {
      if (err) {
        return console.log(err);
      }

      let jsonFileName = groupName.toLowerCase().replace(/\s/g, '-') + ".json";
      let isApplicationType = groupsOfApplication.some(name => name.toLowerCase() === groupName.toLowerCase());

      processJson(jsonFileName, data, isApplicationType);

    });
  });

});


function processJson(fileName, json, isApplicationType) {

  // insert other info such as assembly
  let paths = json['paths'];

  let template = jsyaml.safeLoad(fs.readFileSync(IBM_TEMPLATE_FILE, FILE_ENCODING));

  // ['assembly']['execute'][0] is set-variable, 1 is operation-switch.
  let caseTemplate = template['x-ibm-configuration']['assembly']['execute'][1]['operation-switch']['case'][0];
  let cases = [];

  // loop through each API defined in the json file
  Object.keys(paths).forEach(key => {

    let api = json['paths'][key];
    let operations = [];
    let targetUrl = `${SERVER_URL}${key}`;

    // if it's GET request
    if (api.get) {
      console.log(`GET ${key}`);
      operations.push(api.get.operationId);
    }

    // if it's POST request
    if (api.post) {
      console.log(`POST ${key}`);
      operations.push(api.post.operationId);
    }

    // if it's DELETE request
    if (api.delete) {
      console.log(`DELETE ${key}`);
      operations.push(api.delete.operationId);
    }

    let oneCase = JSON.parse(JSON.stringify(caseTemplate));
    oneCase['operations'] = operations;
    oneCase['execute'][0]['proxy']['target-url'] = targetUrl;
    cases.push(oneCase);

  });

  template['x-ibm-configuration']['assembly']['execute'][1]['operation-switch']['case'] = cases;

  // Object.assign(json, template);
  // 增加OAuth2后变化的地方: 5大处.
  json['x-ibm-configuration'] = template['x-ibm-configuration'];
  json['securityDefinitions'] = template['securityDefinitions'];
  json['security'] = template['security'];
  json['schemes'] = template['schemes'];
  //json['info']['version'] = VERSION;
  json['info']['x-ibm-name'] = json['info']['title'].toLowerCase().replace(/\s/g, '-');

  // application 和 accessCode的不同之处.
  if (isApplicationType) {
    json['securityDefinitions']['OAuth2']['flow'] = 'application';
    delete json['securityDefinitions']['OAuth2']['authorizationUrl'];
  }

  let result = jsyaml.dump(json);

  let outputFile = path.resolve(outputDir, fileName.slice(0, -5) + '.yaml');
  console.log(`output: ${outputFile}\n`);

  fs.writeFileSync(outputFile, result, FILE_ENCODING);
}
