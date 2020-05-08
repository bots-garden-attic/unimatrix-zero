# JavaScript FaaS runtime for Knative

> 🚧 WIP

🐳 Docker image: [https://hub.docker.com/r/k33g/unimatrix-zero-javascript](https://hub.docker.com/r/k33g/unimatrix-zero-javascript)

## How to use it

### Create a function

```bash
docker_user="k33g"
service="unimatrix-zero-js-hello"
namespace="k-apps"

read -d '' CODE << EOF
let handle = params => {
  return {
    message: "👋 Hello World 🌍",
    total: 42
  }
}
EOF

kn service create ${service} \
--namespace ${namespace} \
--env FUNCTION_NAME="hello" \
--env FUNCTION_CODE="$CODE" \
--env README="# Hello World" \
--image docker.io/${docker_user}/unimatrix-zero-javascript:latest \

kn revision list -s ${service} -n ${namespace}
kn route list -n ${namespace}  
```

Call the `hello` function:

```bash
curl -d '{"name":"Bob Morane"}' \
-H "Content-Type: application/json" \
-X POST http://unimatrix-zero-js-hello.k-apps.192.168.64.70.xip.io/hello
```

### Update the function

```bash
read -d '' CODE << EOF
let handle = params => {
  return {
    message: "👋 Hello World 🌍",
    total: 42,
    params: params
  }
}
EOF

kn service update ${service} \
--namespace ${namespace} \
--env FUNCTION_CODE="$CODE" \
--image docker.io/${docker_user}/unimatrix-zero-javascript:latest
```
