const express = require('express')
const fs = require('fs')

const app = express()
const port = process.env.PORT || 8080

//TODO: check value
// the static page could be display something like a README with version of components
const content_type = process.env.CONTENT_TYPE || `text`
const function_name = process.env.FUNCTION_NAME || `hello`
const function_code = process.env.FUNCTION_CODE 
|| 
`
let handle = (params) => {
	return {message:"👋 Hello World 🌍"}
}
exports.handle = handle
`
const readme = process.env.README || `👋 Hello World 🌍`

app.use(express.static('public'))
app.use(express.json())

// Create the file(s)
fs.writeFile('./public/README.md', readme, (err,data) => {
  if (err) {
    return console.log(err)
  }
})

fs.writeFile('./handle.js', function_code, (err,data) => {
  if (err) {
		app.get(`/${function_name}`, (req, res) => {
			res.type('json')
			res.send({ error: err })
		})
		app.post(`/${function_name}`, (req, res, next) => {
			res.type('json')
			res.send({ error: err })
		})
	} else {
		const code = require('./handle')  

		app.get(`/${function_name}`, (req, res) => {
			res.type('json')
			res.send({ message: `🖐️ please use POST to call the ${function_name}`})
		})

		app.post(`/${function_name}`, (req, res, next) => {
			res.type(content_type)
			let params = req.body
			try {
				res.send(code.handle(params))
			} catch(err) {
				if(content_type=="json") {
					res.send({ error: err })
				} else {
					res.send(err)
				}
			}
		})
	}

})

app.listen(port, () => console.log(`🌍 webapp is listening on port ${port}!`))
