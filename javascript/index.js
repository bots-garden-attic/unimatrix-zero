const express = require('express')
const fs = require('fs')

const app = express()
const port = process.env.PORT || 8080

//TODO: check value
// the static page could be display something like a README with version of components
const content_type = process.env.CONTENT_TYPE || `json`
const function_name = process.env.FUNCTION_NAME || `hello`
const function_code = process.env.FUNCTION_CODE 
|| 
`
let ${function_name} = params => {
	return {message:"👋 Hello World 🌍", params: params}
}
`
const readme = process.env.README || `👋 Hello World 🌍`

// Interesting blog post from Axel Rauschmayer
// https://2ality.com/2014/01/eval.html
const code = new Function(`"use strict"; \n${function_code}\nreturn handle`)

//app.use(express.static('public'))
app.use(express.json())

app.post(`/`, (req, res, next) => {
	res.type(content_type)
	let params = req.body

	try {
		res.send(code()(params))
	} catch(err) {
		if(content_type=="json") {
			res.send({ error: err })
		} else {
			res.send(err)
		}
	}
})

app.get(`/`, (req, res) => {
	res.send(readme)
})

app.listen(port, () => console.log(`🌍 JavaScript UniMatrix-Zero runtime for ${function_name} function started on port ${port}!`))
