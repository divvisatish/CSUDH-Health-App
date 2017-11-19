const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref("Notifications")
	.onWrite(event => {
		var request = event.data.val();
		var payload = {
			data: {
				username: "Divvi Enterprise",
				email: "dummyemail@gmail.com"
			}
		};
		admin.messaging().sendToDevice(request.token, payload)
			.then(function (response) {
				console.log("Successfully sent message: ", response);
			})
			.catch(function (error) {
				console.log("Error sending message: ", error);
			})
	})
