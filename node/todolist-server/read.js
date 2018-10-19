const fs = require("fs");

const readFile = function(path) {
	return new Promise((resolve, reject) => {
		fs.readFile(path, (err, data) => {
			if (err) {
				reject(err);
			}

			resolve(data);
		});
	});
}

async function foo() {
    try {
    const data1 = await readFile("index.js");
    console.log(data1);
    const data2 = await readFile("server.js");
    console.log(data2);
    } catch (err) {
        console.error(err);
    }
}