var net = require('net');
const http = require('http');
var exec = require('child_process').exec;
const querystring = require('querystring');
process.env["NODE_TLS_REJECT_UNAUTHORIZED"] = 0;

/// API ////////////////////////////////////////////////////////////////////////

var HOST = 'localhost';
var PORT = 9090;
var REFRESH_MS = 3000;

// var server_ready = 'Tomcat started on port(s):';
// var server_instance = null;
var server_status = true;

// function api_server_backend(port) {
//   return `java -jar -Dserver.port=${port} ioio-core*.jar`;
// }

// FIXME: kill named process
// process.on('exit', function() { server_instance.kill(); });

function api_send(postData, callback) {
  var options = {
    hostname : HOST,
    port : PORT,
    path : '/ioio-core/api/v1/combined',
    method : 'POST',
    headers : {
      'Content-Type' : 'application/json',
      'Content-Length' : Buffer.byteLength(postData)
    },
    rejectUnauthorized : false,
    requestCert : true,
    agent : false
  };

  var req = http.request(options, (res) => {
    console.log(`STATUS: ${res.statusCode}`);
    console.log(`HEADERS: ${JSON.stringify(res.headers)}`);
    res.setEncoding('utf8');
    res.on('data', (chunk) => { callback(chunk); });
    res.on('end', () => { console.log('No more data in response.'); });
  });

  req.on('error',
         (e) => { console.log(`problem with request: ${e.message}`); });

  req.write(postData);
  req.end();
}

function api_check_port() {
  var server = net.createServer();

  gui_process_start()

  server.once('error', function(err) {
    if (err.code === 'EADDRINUSE') {
      gui_process_end()
      server_status = true;
    }
  });

  server.once('listening', function() {
    if (server_status == true)
      alert("`ioio-core` is not running! Start it manually.");
    server_status = false;
    server.close();
  });

  server.listen(PORT);
}

// function api_start_server() {
//   server_instance =
//       exec(api_server_backend(PORT),
//            function(error, stdout, stderr) { console.log(stdout); });
//   server_instance.stdout.on('data', function(data) {
//     console.log(data);
//     if (data.includes(server_ready)) {
//       console.log("\033[92mREADY");
//       gui_process_end();
//     }
//   });
// }

/// GUI ////////////////////////////////////////////////////////////////////////

function gui_process_start() {
  document.querySelector("body").setAttribute("class", "bg-danger")
}

function gui_process_end() {
  document.querySelector("body").setAttribute("class", "")
}

function gui_update(body) {
  console.log("BODY -->", body);
  $('#json').val(body);
  gui_process_end();
}

function gui_set_modifiers(val_modifiers) {
  console.log(val_modifiers);
  $('#modifiers').val(JSON.stringify(val_modifiers));
}

function gui_minify() {
  gui_set_modifiers([ {"params" : "string", "type" : "minify"} ])
  gui_run();
}

function gui_maxify() {
  gui_set_modifiers([ {"params" : "string", "type" : "maxify"} ])
  gui_run();
}

function gui_run() {
  val_json = $("#json").val()
  val_modifiers = JSON.parse($("#modifiers").val())
  val_request_data = {"json" : val_json, "modifiers" : val_modifiers}

  postData = JSON.stringify(val_request_data);
  gui_process_start();
  api_send(postData, gui_update);
}

function gui_host() {
  // if (server_instance != null || server_status) {
  //   alert("`ioio-core` is already running!");
  //   return;
  // }

  val_plain_host = $("#host").val().split(':');
  HOST = val_plain_host[0]
  PORT = val_plain_host[1]
  console.log(HOST, PORT);

  server_status = true;
  gui_process_start();
  // api_start_server();
}

////////////////////////////////////////////////////////////////////////////////

window.addEventListener('DOMContentLoaded', () => {
  api_check_port()
setInterval(function() { api_check_port() }, REFRESH_MS)

document.querySelector("#start").addEventListener("click",
                                                  function() { gui_host() })

document.querySelector("#run").addEventListener("click",
                                                function() { gui_run() })

document.querySelector("#minify").addEventListener("click",
                                                   function() { gui_minify() })

  document.querySelector("#maxify").addEventListener("click", function() {
  gui_maxify();
  })
})
