const express = require("express");
const path = require("path");
const mongoose = require("mongoose");
const app = express();
const upload = require("./config/Upload");
const uri =
  "mongodb+srv://nmchien2204:chien1234@cluster0.4b0kubq.mongodb.net/md18305";
const users = require("./models/user");

const svModel = require("./studentModel");
const apiRouter = require("./api");

app.use(express.json());
app.use("/uploads", express.static(path.join(__dirname, "public/uploads"))); // Serve uploaded files
app.use("/api", apiRouter);

const port = 3000;
app.listen(port, () => {
  console.log(`Example app listening on port ${port}`);
});

// Connect to MongoDB
mongoose
  .connect(uri, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => console.log("Connect success"))
  .catch((err) => console.log("Connect failed:", err));

app.get("/", async (req, res) => {
try {
  let svien = await svModel.find();
  res.send(svien);
} catch (error) {
  console.log("lỗi ");
}
});

app.post("/add_sv", upload.single("image"), async (req, res) => {
  try { 
    const data = req.body;
    const { file } = req;
    const urlsImage =  `${req.protocol}://${req.get("host")}/uploads/${file.filename}`
  
    const newStudent = new svModel({
      name: data.name,
      tuoi: data.tuoi,
      mssv: data.mssv,
      image: urlsImage,
    });

    await newStudent.save();

    res.send(newStudent);
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
});

app.put("/update/:id", upload.single("image"), async (req, res) => {
  try {
    const data = req.body;
    const studentId = req.params.id;
    const { file } = req;

    const imageUrl = `${req.protocol}://${req.get("host")}/uploads/${
      file.filename
    }`;

    const updatedStudent = await svModel.findByIdAndUpdate(studentId, {
      name: data.name,
      tuoi: data.tuoi,
      mssv: data.mssv,
      image: imageUrl,
    });

    if (updatedStudent) {
      res.json({
        status: 200,
        message: "Cập nhật thành công",
        data: updatedStudent,
      });
    } else {
      res.json({
        status: 400,
        message: "Không tìm thấy sinh viên",
        data: [],
      });
    }
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
});

app.delete("/delete/:id", async (req, res) => {
  try {
    const id = req.params.id;
    await svModel.deleteOne({ _id: id });
    const sinhviens = await svModel.find();
    console.log(sinhviens);
    res.send(sinhviens);
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
});

app.post("/register-send-email", upload.single("avartar"), async (req, res) => {
  try {
    const data = req.body;
    const { file } = req;
    const avatar = `${req.protocol}://${req.get("host")}/uploads/${
      file.filename
    }`;
    const newUser = users({
      username: data.username,
      password: data.password,
      email: data.email,
      name: data.name,
      avartar: avatar,
    });
    const result = await newUser.save();
    if (result) {
      res.json({
        status: 200,
        messenger: "Thêm thành công",
        data: result,
      });
    } else {
      res.json({
        status: 400,
        messenger: "Lỗi, thêm không thành công",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
  }
});

app.post("/login", async (req, res) => {
  try {
    const { username, password } = req.body;
    const user = await users.findOne({ username, password });
    if (user) {
      res.json({
        status: 200,
        messenger: "Đăng nhâp thành công",
        data: user,
      });
    } else {
      res.json({
        status: 400,
        messenger: "Lỗi, đăng nhập không thành công",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
  }
});
app.put("/update-no-image/:id", upload.array("image", 5), async (req, res) => {
  try {
    const svId = req.params.id;
    const data = req.body;

    const result = await svModel.findByIdAndUpdate(svId, {
      name: data.name,
      tuoi: data.tuoi,
      mssv: data.mssv,
    });

    if (result) { 
      res.json({
        status: 200,
        messenger: "Cập nhật thành công",
        data: result,
      });
    } else {
      res.status(404).json({
        status: 404,
        messenger: "Không tìm thấy sinh viên",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
    res.status(500).json({
      status: 500,
      messenger: "Lỗi, không thể cập nhật sinh viên",
      data: [],
    });
  }
});

app.get("/search", async (req, res) => {
  try {
    const tuKhoa = req.query.key;

    const ketQuaTimKiem = await svModel.find({
      name: { $regex: new RegExp(tuKhoa, "i") },
    });

    if (ketQuaTimKiem.length > 0) {
      res.json(ketQuaTimKiem);
    } else {
      res.json([]);
    }
  } catch (error) {
    res.status(500).send("Lỗi máy chủ nội bộ");
  }
});

const sapxepgiamdan = (products) => {
  return products.sort((a, b) => b.tuoi - a.tuoi);
};

app.get("/giam-dan", async (req, res) => {
  try {
    let svien = await svModel.find();
    let sortedProducts = sapxepgiamdan(svien);
    res.json(sortedProducts);
  } catch (error) {
    console.error(
      "Lỗi khi sắp xếp danh sách sản phẩm theo giá tăng dần:",
      error
    );
    res.status(500).send("Đã xảy ra lỗi khi sắp xếp danh sách sản phẩm");
  }
});

const sapxeptangdan = (products) => {
  return products.sort((a, b) => a.tuoi - b.tuoi);
};

app.get("/tang-dan", async (req, res) => {
  try {
    let svien = await svModel.find();
    let sortedProducts = sapxeptangdan(svien);
    res.json(sortedProducts);
  } catch (error) {
    console.error(
      "Lỗi khi sắp xếp danh sách sản phẩm theo giá giảm dần:",
      error
    );
    res.status(500).send("Đã xảy ra lỗi khi sắp xếp danh sách sản phẩm");
  }
});

module.exports = app;
