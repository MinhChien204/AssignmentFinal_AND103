const mongoose = require('mongoose');
mongoose.set('strictQuery', true);
const atlas = "mongodb+srv://nmchien2204:chien1234@cluster0.4b0kubq.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

const connect = async () => {
  try {
    await mongoose.connect(atlas, {
      useNewUrlParser: true,
      useUnifiedTopology: true
    }); 
    console.log('Connect success');
  } catch (error) {
    console.log(error);
    console.log('Connect fail');
  }
};

module.exports = { connect };