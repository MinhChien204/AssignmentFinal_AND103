
var fs = require('fs');
const mongoose = require('mongoose');
const { type } = require('os');

const StudentSchema = mongoose.Schema({
  name: {
    type: String,
  },
  tuoi: {
    type: Number,
  },
  mssv: {
    type: String,
  },
  image:{
    type:String,
  },
  daRaTruong: {
    type: Boolean,
  }
});

const svModel = mongoose.model('students', StudentSchema);

module.exports = svModel;