const express = require('express')
const fs = require('fs')

const app = express()
const port = process.env.PORT || 8080

//TODO: check value
// GET (void), POST (with parameters)
// format: json, text, html, ...
// the static page could be display something like a README with version of components
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
		// TODO: ?
    return console.log(err) // don't stop?
  }
	console.log(data)
})


fs.writeFile('handle.js', function_code, (err,data) => {
  if (err) {
		// TODO: ?
    return console.log(err) // don't stop?
	}

	const code = require('./handle')
  
	console.log(data)
	app.get(`/${function_name}`, (req, res) => {

		res.send(code.handle())
	})
})


app.listen(port, () => console.log(`🌍 webapp is listening on port ${port}!`))
