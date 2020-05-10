# Unimatrix-Zero

**Unimatrix-Zero** is a set of "FaaS runtimes" to deploy quickly small functions on **Knative** and benefit of all qualities of **Knative** (serving and eventing).

> eventing part is in progress

🖐️ **Unimatrix-Zero** runtimes are only intended to experiments easily with **Knative**

## Requirements

- Knative serving
- Knative eventing (right now, optional)
- Install **kn** (the Knative CLI)

## Runtimes

- JavaScript: [javascript/README.md](javascript/README.md)
- Kotlin: [kotlin/README.md](kotlin/README.md)

### Example: JavaScript (NodeJS)

Right now, it's the only one.

#### How to use it

##### Create a function

```bash
docker_user="k33g"
service="hello-node"
namespace="k-apps" # create the namespace before

read -d '' CODE << EOF
let hello = params => {
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
-X POST http://unimatrix-zero-js-hello.k-apps.192.168.64.70.xip.io
```

> Remark: if you use a GET request, you'll get the content of the `README` environment variable

##### Update the function

```bash
read -d '' CODE << EOF
let hello = params => {
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
