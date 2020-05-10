# GraalVM FaaS runtime for Knative

> 🚧 WIP

🐳 Docker image: [https://hub.docker.com/r/k33g/unimatrix-zero-polyglot](https://hub.docker.com/r/k33g/unimatrix-zero-polyglot)

## How to use it

You can create JavaScript, Ruby or Python functions

### Ruby

> hello.rb

```ruby
def hello(params)
  return "🌍 Name= " + params.getString("name")
end
# params is a io.vertx.core.json.JsonObject
```

> run this:

```bash
docker_user="k33g" # replace by your username on Docker Hub
service="ruby-hello"
namespace="k-apps"

# create or update the service
kn service create --force ${service} \
--namespace ${namespace} \
--env FUNCTION_NAME="hello" \
--env LANG="ruby" \
--env README="# this is a ruby function" \
--env FUNCTION_CODE="$(cat ./hello.rb)" \
--env CONTENT_TYPE="plain/text;charset=UTF-8" \
--image docker.io/${docker_user}/unimatrix-zero-polyglot:latest
```

Call the `hello` function with POST request:

```bash
curl -d '{"name":"Bob"}' \
-H "Content-Type: application/json" \
-X POST http://ruby-hello.k-apps.192.168.64.70.xip.io
```

> Remark: if you use a GET request, you'll get the content of the `README` environment variable

### Python

> hello.py

```python
def hello(params):
    return "Name is " + params.getString("name")

# params is a io.vertx.core.json.JsonObject
```

> run this:

```bash
docker_user="k33g" # replace by your username on Docker Hub
service="python-hello"
namespace="k-apps"

# create or update the service
kn service create --force ${service} \
--namespace ${namespace} \
--env FUNCTION_NAME="hello" \
--env LANG="python" \
--env README="# this is a python function" \
--env FUNCTION_CODE="$(cat ./hello.py)" \
--env CONTENT_TYPE="plain/text;charset=UTF-8" \
--image docker.io/${docker_user}/unimatrix-zero-polyglot:latest
```

> Call the function

```bash
curl -d '{"name":"Bob"}' \
-H "Content-Type: application/json" \
-X POST http://python-hello.k-apps.192.168.64.70.xip.io
```

### JavaScript

> hello.js

```javascript
function hello(params) {
  return {
    message: "Hello World",
    total: 42,
    author: "@k33g_org",
    params: params.getString("name")
  }
}
// params is a io.vertx.core.json.JsonObject
```

> run this:

```bash
docker_user="k33g" # replace by your username on Docker Hub
service="javascript-hello"
namespace="k-apps"

# create or update the service
kn service create --force ${service} \
--namespace ${namespace} \
--env FUNCTION_NAME="hello" \
--env LANG="js" \
--env README="# this is a JavaScript function" \
--env FUNCTION_CODE="$(cat ./hello.js)" \
--env CONTENT_TYPE="application/json;charset=UTF-8" \
--image docker.io/${docker_user}/unimatrix-zero-polyglot:latest
```

> Call the function

```bash
curl -d '{"name":"Bob"}' \
-H "Content-Type: application/json" \
-X POST http://javascript-hello.k-apps.192.168.64.70.xip.io
```
