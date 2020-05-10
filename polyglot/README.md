# Kotlin FaaS runtime for Knative

> 🚧 WIP

🐳 Docker image: [https://hub.docker.com/r/k33g/unimatrix-zero-kotlin](https://hub.docker.com/r/k33g/unimatrix-zero-kotlin)

## How to use it

### Create a function

```bash
docker_user="k33g"
service="hello-kotlin"
namespace="k-apps"

read -d '' CODE << EOF
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

fun hello(params: Any): Any {
  return json {
    obj(
      "message" to "Hello World!!!",
      "total" to 42
    )
  }.encodePrettily()
}
EOF

kn service create ${service} \
--namespace ${namespace} \
--env FUNCTION_NAME="hello" \
--env FUNCTION_CODE="$CODE" \
--env README="# Hello World" \
--image docker.io/${docker_user}/unimatrix-zero-kotlin:latest \

kn revision list -s ${service} -n ${namespace}
kn route list -n ${namespace}
```

Call the `hello` function with POST request:

```bash
curl -d '{"name":"Bob Morane"}' \
-H "Content-Type: application/json" \
-X POST http://unimatrix-zero-kotlin-hello.k-apps.192.168.64.70.xip.io
```

> Remark: if you use a GET request, you'll get the content of the `README` environment variable

### Update the function

```bash
read -d '' CODE << EOF
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

fun hello(params: Any): Any {
  return json {
    obj(
      "message" to "Hello World!!!",
      "total" to 42,
      "params" to params
    )
  }.encodePrettily()
}
EOF

kn service update ${service} \
--namespace ${namespace} \
--env FUNCTION_CODE="$CODE" \
--image docker.io/${docker_user}/unimatrix-zero-kotlin:latest
```
