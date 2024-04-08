const express = require('express');

const router = express.Router();


const mongoose = require('mongoose');
const uri = 'mongodb+srv://nmchien2204:chien1234@cluster0.4b0kubq.mongodb.net/md18305';
const svModel = require('./studentModel');

router.get('/', async (req, res)=>{
  await mongoose.connect(uri);

  let sinhviens = await svModel.find();
  console.log(sinhviens);

  res.send(sinhviens);
})

module.exports = router;