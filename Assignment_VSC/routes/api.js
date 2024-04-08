var express = require('express')
var routes = express.Router()

const multer  = require('multer')
var objUpload = multer( { dest: './tmp'}); 

//thêm model
const Distributors = require('../models/distributors')
const Fruits = require('../models/fruit')
const { route } = require('.')
//Api thêm distributor
routes.post('/add-distributor',async  (req, res)=>{
  try {
    const data = req.body;//lấy dữ liêu từ body
    const newDistributor = new Distributors({
      name:data.name
    })
    const result = await newDistributor.save()
    if(result){
      //nếu thêm thành công result !null trả về dữ liệu
      res.json({
        "status":200,
        "message":"Thêm thành công",
        "data":result
      })
    }else{
      //nếu thêm không thành công result null , thông báo không thành công
      res.json({
        "status":400,
        "message":"Thêm không thành công",
        "data":[]
      })
    }
  } catch (error) {
    console.log(error);
  }
})

//Api thêm fruit
routes.post('/add-fruit',async (req,res)=>{
  try {
    const data = req.body;
    const newFruit = new Fruits({
      name:data.name,
      quantity:data.quantity,
      price:data.price,
      status:data.status,
      image:data.image,
      description:data.description,
      id_distributor:data.id_distributor,
    })
    const result = await newFruit.save();
    if(result){
      //nếu thêm thành công result!null trả về dữ liệu
      res.json({
        "status":200,
        "message":"Thêm thành công",
        "data":result
      })
    }else{
        //nếu thêm thành công result!null trả về dữ liệu
        res.json({
          "status":400,
          "message":"Lỗi,Thêm không thành công",
          "data":[]
        })
    }
  } catch (error) {
    console.log(error);
  }
})
//truy vấn danh sách
routes.get('/get-list-fruit',async (req, res) =>{
try {
  const data  = await Fruits.find().populate('id_distributor')
  res.json({
    "status":200,
    "message":" danh sách fruit",
    "data":data
  })
} catch (error) {
  console.log(error);
}
})
routes.get('/get-fruit-by-id/:id',async  (req, res)=>{
  try {
    const {id} = req.params.id;
    const data = await Fruits.findById(id).populate('id_distributor')
    res.json({
      "status":200,
      "message":" danh sách fruit",
      "data":data
    })
  } catch (error) {
    console.log(error);
  }
})
module.exports = routes.post('/upload',objUpload.single('hinh_anh'),upload)