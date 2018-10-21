const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

require('dotenv').config();
require('./models/Users');

mongoose.connect(process.env.MONGO_URI, { useNewUrlParser: true }, err => {
	if (err) console.log('Error: ', err)
});

const app = express();

app
	.use(bodyParser.json())
	.use(bodyParser.urlencoded({ extended: false }));

// Routes------------------------------------------------------
app
	.post('/addUser', (req, res) => {
		const User = mongoose.model('users');

		User
			.findOne( { username: req.body.username } )
			.then( isExists => {
				if ( !isExists ) {
					new User({
						name: req.body.name,
						username: req.body.username,
						password: req.body.password
					})
					.save( (err, user) => {
						if (err) res.json({ Error: err });
						res.json({ isSaved: true });
					});
				}
				else
					res.json( { isSaved: false } );
			})
	})
	.get('/getAllUsers', (req, res) => {
		const User = mongoose.model('users');

		User.find((err, users) => {
			if (err) res.json({ Error: err });
			res.json( { users } );
		})
	})
	.get('/getUser/:username', (req, res) => {
		const User = mongoose.model('users');

		User
			.find( { username : req.params.username } )
			.exec( (err, users) => {
				if (err) res.json({ Error: err });
				res.json( { users } );	
			})
	})
	.post('/authUser', (req, res) => {
		const User = mongoose.model('users');

		console.log(req.body)
		User
			.find( { username : req.body.username } )
			.where('password').equals(req.body.password)
			.exec( (err, user) => {
				if (err) res.json({ Error: err });
				res.json( user );	
			})	
	})
	.listen(process.env.PORT || 3000);