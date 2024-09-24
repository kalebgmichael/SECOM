
    // Function to display received messages
    function showMessage(message) {
        const messageElement = document.createElement('div');
        messageElement.textContent = message;
        document.getElementById('messages').appendChild(messageElement);
    }

    function showMessage_private(message) {
        const messageElement = document.createElement('div');
        messageElement.textContent = message;
        document.getElementById('private-messages').appendChild(messageElement);
    }

    function showMessage_Encrypted_messages(message) {
        const messageElement = document.createElement('div');
        messageElement.textContent = message;
        document.getElementById('Encrypted-messages').appendChild(messageElement);
    }

    function handleSecretMessage(secretMessage) {
        secretmessage_new = secretMessage;
        console.log("secretmessage_new", secretmessage_new);
        // Here you can do whatever you want with the secret message


            const url = `/api/socket/getsharedkey?Recid=${encodeURIComponent(recId)}&SenderId=${encodeURIComponent(senderId)}&publicKey=${encodeURIComponent(publicKey)}`;
            const ownerId = document.getElementById('ownerId').innerText;

                 // Make AJAX request to server endpoint with private key
            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch data');
                    }
                    return response.text();
                })
                .then(secretMessage => {
                secretmessage_new = secretMessage;
                    showMessage('Secret message from getsharedkey: ' + secretMessage);
                     console.log("secretmessage_new from getsharedkey", secretmessage_new);
                })
                .catch(error => {
                    console.error('Error:', error);
                });



    }

function handleSharedkey(secretMessage) {
        secretmessage_new = secretMessage;
        console.log("secretmessage_new in handlesharedkey", secretmessage_new);
        // Here you can do whatever you want with the secret message
            const publicKey = secretmessage_new.publicKey;
            console.log("publickey"+publicKey);

            const senderId = secretmessage_new.senderId;
            console.log("senderId"+senderId);
            const recId = secretmessage_new.recId;
            console.log("recId"+recId);
            const time = secretmessage_new.time;

            const url = `/api/socket/getsharedkey?Recid=${encodeURIComponent(recId)}&SenderId=${encodeURIComponent(senderId)}&publicKey=${encodeURIComponent(publicKey)}`;
            const ownerId = document.getElementById('ownerId').innerText;

                 // Make AJAX request to server endpoint with private key
            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch data');
                    }
                    return response.text();
                })
                .then(secretMessage => {
                secretmessage_new = secretMessage;
                    showMessage('Secret message from getsharedkey: ' + secretMessage);
                     console.log("secretmessage_new from getsharedkey", secretmessage_new);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
                }

function  handlePublicKey(secretMessage)
          {
          const ownerId1=secretMessage.recId
          secretmessage_new = secretMessage;
           console.log("secretmessage_new in hadnlepublickey"+ownerId1+"is"+secretmessage_new);
           // Extract values from the message body
            const dh_publicKey = secretMessage.dh_Pubkey;
            console.log("dh_publicKey"+secretmessage_new.dh_Pubkey);
            const ca_Pubkey = secretMessage.ca_Pubkey;
            console.log("ca_Pubkey"+ca_Pubkey);
            const senderId = secretMessage.senderId;
             console.log("senderId"+senderId);
            const recId = secretMessage.recId;
             console.log("recId"+recId);
            const time = secretMessage.time;
             console.log("time"+time);

        // Here you can do whatever you want with the secret message

            const url = `/api/socket/rec_dh_pub_key?encryptedmessage=${encodeURIComponent(dh_publicKey)}&publickey=${encodeURIComponent(ca_Pubkey)}&sendid=${encodeURIComponent(senderId)}&peerid=${encodeURIComponent(recId)}`;
            const ownerId = document.getElementById('ownerId').innerText;

                 // Make AJAX request to server endpoint with private key
            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch data');
                    }
                    return response.text();
                })
                .then(secretMessage => {
                secretmessage_new = secretMessage;
                    showMessage('Secret message from handlepublic key: ' + secretMessage);
                     console.log("secretmessage_new from rec_dh_pub_key"+ secretMessage);
                     // Parse the JSON string into a JavaScript object
                const secretMessageObject = JSON.parse(secretmessage_new);

                // Access the publicKey property
                const publicKey = secretMessageObject.publicKey;
                console.log("publicKey"+publicKey);
                     handleSharedkey(secretMessageObject);

                })
                .catch(error => {
                    console.error('Error:', error);
                });

          }
function  handlePublicKey_rec(secretMessage)
          {
          const ownerId1=secretMessage.recId
          secretmessage_new = secretMessage;
           console.log("secretmessage_new in hadnlepublickey"+ownerId1+"is"+secretmessage_new);
           // Extract values from the message body
            const dh_publicKey = secretMessage.dh_Pubkey;
            console.log("dh_publicKey"+secretmessage_new.dh_Pubkey);
            const ca_Pubkey = secretMessage.ca_Pubkey;
            console.log("ca_Pubkey"+ca_Pubkey);
            const senderId = secretMessage.senderId;
             console.log("senderId"+senderId);
            const recId = secretMessage.recId;
             console.log("recId"+recId);
            const time = secretMessage.time;
             console.log("time"+time);

        // Here you can do whatever you want with the secret message

            const url = `/api/socket/rec_dh_pub_key_rec?encryptedmessage=${encodeURIComponent(dh_publicKey)}&sendid=${encodeURIComponent(senderId)}&peerid=${encodeURIComponent(recId)}`;
            const ownerId = document.getElementById('ownerId').innerText;

                 // Make AJAX request to server endpoint with private key
            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch data');
                    }
                    return response.text();
                })
                .then(secretMessage => {
                secretmessage_new = secretMessage;
                    showMessage('Secret message from handlepublic key: ' + secretMessage);
                     console.log("secretmessage_new from rec_dh_pub_key"+ secretMessage);
                     // Parse the JSON string into a JavaScript object
                const secretMessageObject = JSON.parse(secretmessage_new);

                // Access the publicKey property
                const publicKey = secretMessageObject.publicKey;
                console.log("publicKey"+publicKey);
                     handleSharedkey(secretMessageObject);

                })
                .catch(error => {
                    console.error('Error:', error);
                });

          }



    // Connect to WebSocket server
    var secretmessage_new = "";
    const socket = new SockJS('/chat-websocket');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        // Check if headers are available
    if (frame && frame.headers) {
        const headers = frame.headers;
        console.log(headers);
        // Access individual headers
        console.log('User name:', headers['user-name']);
        console.log('Other header:', headers['ownerId']);
        // You can access other headers similarly
    }

        // Subscribe to the topic to receive messages
        stompClient.subscribe('/topic/chat', function (message) {

            console.log("message from topic "+message)
<!--            showMessage('Message received: ' + message.body);-->
            // Extract private key from the received message
            const privateKey = JSON.parse(message.body).text;



<!--            // Make AJAX request to server endpoint with private key-->
<!--            fetch('/api/socket/data?privatekey=' + encodeURIComponent(privateKey))-->

<!--                .then(response => {-->
<!--                    if (!response.ok) {-->
<!--                        throw new Error('Failed to fetch data');-->
<!--                    }-->
<!--                    return response.text();-->
<!--                })-->
<!--                .then(secretMessage => {-->
<!--                    showMessage('Secret message: ' + secretMessage);-->
<!--                })-->
<!--                .catch(error => {-->
<!--                    console.error('Error:', error);-->
<!--                });-->
        });

          // Subscribe to the topic to receive messages
        stompClient.subscribe('/topic/public-key', function (message) {

            console.log("message from topic "+message)
<!--            showMessage('Message received: ' + message.body);-->
            // Extract private key from the received message

             // Parse the JSON message body
             const messageBody = JSON.parse(message.body);


         // Extract values from the message body
            const publicKey = messageBody.publicKey;
            console.log("publickey"+publicKey);
            const senderId = messageBody.senderId;
            const recId = messageBody.recId;
            const time = messageBody.time;

            // Print the extracted values
<!--            console.log("Public Key1: " + encodeURIComponent(publicKey));-->
              console.log("senderId"+senderId);
              console.log("recId"+recId);

            // Make AJAX request to server endpoint with private key
            // Construct the URL with query parameters using backticks (`) and ${} syntax for interpolation

            const url = `/api/socket/getsharedkey?Recid=${encodeURIComponent(recId)}&SenderId=${encodeURIComponent(senderId)}&publicKey=${encodeURIComponent(publicKey)}`;
            const ownerId = document.getElementById('ownerId').innerText;
            if(ownerId===recId)
            {
                 // Make AJAX request to server endpoint with private key
            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch data');
                    }
                    return response.text();
                })
                .then(secretMessage => {
                secretmessage_new = secretMessage;
                    showMessage('Secret message: ' + secretMessage);
                    console.log("secretmessage_new"+secretmessage_new);
                      handleSecretMessage(secretMessage);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
            }





        });

        stompClient.subscribe('/topic/public_key_ca', function (message) {
            console.log("message from topic public_key_ca "+message)
            // Extract private key from the received message

            // Parse the JSON message body
             const messageBody = JSON.parse(message.body);

         // Extract values from the message body
            const dh_publicKey = messageBody.dh_Pubkey;
            console.log("dh_publicKey"+dh_publicKey);
            const ca_Pubkey = messageBody.ca_Pubkey;
            console.log("ca_Pubkey"+ca_Pubkey);
            const senderId = messageBody.senderId;
            const recId = messageBody.recId;
            const time = messageBody.time;

            // Print the extracted values
              console.log("senderId"+senderId);
              console.log("recId"+recId);
              console.log("this is public_key_ca_topic");
              if(ownerId===recId)
                {

                 handlePublicKey(messageBody);
                }

        });

             stompClient.subscribe('/topic/public_key_ca_rec', function (message) {
            console.log("message from topic public_key_ca "+message)
            // Extract private key from the received message

            // Parse the JSON message body
             const messageBody = JSON.parse(message.body);

         // Extract values from the message body
            const dh_publicKey = messageBody.dh_Pubkey;
            console.log("dh_publicKey"+dh_publicKey);
            const ca_Pubkey = messageBody.ca_Pubkey;
            console.log("ca_Pubkey"+ca_Pubkey);
            const senderId = messageBody.senderId;
            const recId = messageBody.recId;
            const time = messageBody.time;

            // Print the extracted values
              console.log("senderId"+senderId);
              console.log("recId"+recId);
              if(ownerId===recId)
                {
                    console.log("this is public_key_ca_rec");
                 handlePublicKey_rec(messageBody);
                }

        });


                  // Subscribe to the topic to receive messages
        stompClient.subscribe('/topic/peer-public-key', function (message) {
                    console.log("message from topic peer-publice "+message)
                    // Extract private key from the received message
                    const messageBody = JSON.parse(message.body);
                    // Extract values from the message body
                    const publicKey = messageBody.publicKey;
                    console.log("publickey"+publicKey);
                    const senderId = messageBody.senderId;
                    const recId = messageBody.recId;
                    const time = messageBody.time;

                    // Print the extracted values
                    console.log("senderId"+senderId);
                    console.log("recId"+recId);

                    // Make AJAX request to server endpoint with private key
                    // Construct the URL with query parameters using backticks (`) and ${} syntax for interpolation
            const url = `/api/socket/getkey_own_dh?Recid=${encodeURIComponent(senderId)}&publickey=${encodeURIComponent(publicKey)}`;
            const ownerId = document.getElementById('ownerId').innerText;
            if(ownerId===recId)
            {
                 // Make AJAX request to server endpoint with private key
            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch data');
                    }
                    return response.text();
                })
                .then(secretMessage => {
                secretmessage_new = secretMessage;
                    console.log("secretmessage_new in "+ownerId+"is"+secretmessage_new);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
            }

                    });

        console.log("secretmessage_new"+secretmessage_new);

        // subscribe to Receive Encrypted message
         // Subscribe to the topic to receive messages
        stompClient.subscribe('/topic/EncryptedMessage', function (message_enc) {

            console.log("message from topic "+message_enc)

            // Extract private key from the received message

             // Parse the JSON message body
             const messageBody_enc1 = JSON.parse(message_enc.body);


         // Extract values from the message body
             const SenderId_enc = messageBody_enc1.senderId;
             const RecId_enc = messageBody_enc1.recId;
            const message_enc1 = messageBody_enc1.message;
             console.log("Encrypted message"+message_enc1);
            const time_enc = messageBody_enc1.time;

            // Print the extracted values
              console.log("SenderId"+ SenderId_enc);
              console.log("RecId"+ RecId_enc);
               console.log("RecId"+ time_enc);
               var secretkeypass="Di3jyJYxTquIuVMvRrn6Ng==";
               if(ownerId===RecId_enc)
               {
                   showMessage_Encrypted_messages('Message received: ' + message_enc.body);
                   // Make AJAX request to server endpoint with private key
            // Construct the URL with query parameters using backticks (`) and ${} syntax for interpolation

            const url1 = `/api/socket/getMessageDecrypted?encryptedMessage=${encodeURIComponent(message_enc1)}&senderid=${encodeURIComponent(SenderId_enc)}
            &secretkey=${encodeURIComponent(secretkeypass)}&peerid=${encodeURIComponent(RecId_enc)}`;

             const url = `/api/socket/MessageDecrypted?encryptedMessage=${encodeURIComponent(message_enc1)}&senderid=${encodeURIComponent(SenderId_enc)}
            &secretkey=${encodeURIComponent(secretkeypass)}&peerid=${encodeURIComponent(RecId_enc)}`;
            const ownerId = document.getElementById('ownerId').innerText;

                 // Make AJAX request to server endpoint with private key
            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch data');
                    }
                    return response.text();
                })
                .then(secretMessage => {
                secretmessage_new = secretMessage;
<!--                    showMessage('Secret message: ' + secretMessage);-->
                    showMessage_Encrypted_messages('This is the secret message'+secretMessage);
                    console.log("secretmessage_new"+secretmessage_new);
                })
                .catch(error => {
                    console.error('Error:', error);
                });

               }
    });

    });


   document.getElementById('encryptionForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const encryptedMessage = document.getElementById('encryptedMessage').value;
        const senderid = document.getElementById('senderid').value;
        const secretkey = document.getElementById('secretkey').value;
        const peerid = document.getElementById('peerid').value;

        // Construct the URL with parameters included in the query string
        const url = `http://localhost:8989/api/socket/Encrypt?encryptedMessage=${encodeURIComponent(encryptedMessage)}&senderid=${encodeURIComponent(senderid)}&secretkey=${encodeURIComponent(secretkey)}&peerid=${encodeURIComponent(peerid)}`;

        // Make a GET request to the server`
        fetch(url)
          .then(response => response.json()) // Parsing the JSON response body
          .then(data => {
              console.log('Success:', data);
              alert('Encryption Successful!'); // Alerting the user upon successful operation
          })
          .catch((error) => {
              console.error('Error:', error); // Handling errors in the request
          });
    });