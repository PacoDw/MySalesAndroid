const mongoose = require('mongoose');

const usersSchema = new mongoose.Schema({
    id_user  : Number,
    name     : String,
    username : String,
    password : String
});

mongoose.model('users', usersSchema);