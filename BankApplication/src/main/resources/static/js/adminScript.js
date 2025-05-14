var totalBalance = 0.00;
var count = 0;
var totalCreditAmount = 0.00;
var totalOutstandingAmount =0.00;
// api fetch
console.log("api start");
var bankBalance= document.getElementById("total-balance");
var accountholders = document.getElementById("total-account-holders");
var totalCredit = document.getElementById("total-credit-balance");
var totalOutstanding = document.getElementById("total-outstanding");
var customerTableBody = document.getElementById("customer-details-table");
fetch("https://satisfied-recreation-production.up.railway.app/api/customer")
.then(response => response.json())
.then(data =>{
    data.forEach(items =>{
            console.log("balance details: " + items.balanceDetails.accountBalance);
            totalBalance += items.balanceDetails.accountBalance;
            count++;
            totalCreditAmount += items.balanceDetails.creditCardBalance;
            totalOutstandingAmount += items.balanceDetails.outstandingBalance;

//          customer details table
            const tableRow = document.createElement("tr");
            tableRow.innerHTML = `
             <td>${items.lastName}</td>
             <td>${items.accountDetails[0].accountNumber}</td>
             <td>${items.balanceDetails.accountBalance}</td>
             <td><a href="/bank/user/balance/${items.id}" class="update-button">Update</a></td>`;
             customerTableBody.appendChild(tableRow);

             var openingContainer = document.querySelector('.account-opening-container');

//             Account opening
             if(items.opening_action === "NOT_APPROVED"){
                 openingContainer.style.display= 'flex';
                 var createAccountOpeningContainer = document.createElement("div");
                 createAccountOpeningContainer.innerHTML = `
                           <form action="/bank/decline/${items.id}" method="get">
                                 <div class="row">
                                       <label>Name : </label>
                                       <input type="text" value="${items.lastName}">
                                 </div>
                                 <div class="row">
                                        <label>Phone : </label>
                                        <input type="text" value="${items.phoneNumber}" >
                                 </div>
                                 <div class="row">
                                          <label>PAN : </label>
                                          <input type="text" value="${items.pan}">
                                 </div>
                                 <div class="row">
                                          <label>Aadhaar : </label>
                                          <input type="text" value="${items.aadhar}">
                                 </div>
                                 <div class="row">
                                          <a href="/bank/account/Approval/${items.id}">Approval</a>
                                          <input type="submit" value="Decline" class="decline-button">
                                 </div>
                           </form>
                 `;
                 createAccountOpeningContainer.classList.add('account-opening');

                 openingContainer.appendChild(createAccountOpeningContainer);
             }
//             Account closing
             var closingContainer = document.querySelector('.account-closing-container');
             closingContainer.style.display= 'none';
             if(items.opening_action === "CLOSE_REQUEST"){
                             closingContainer.style.display= 'flex';
                             var createAccountClosingContainer = document.createElement("div");
                             createAccountClosingContainer.innerHTML = `
                                       <form action="/bank/reject/${items.id}" method="get">
                                             <div class="row">
                                                   <label>Name : </label>
                                                   <input type="text" value="${items.lastName}">
                                             </div>
                                             <div class="row">
                                                    <label>Account : </label>
                                                    <input type="text" value="${items.accountDetails[0].accountNumber}" >
                                             </div>
                                             <div class="row">
                                                      <label>Reason : </label>
                                                      <input type="text" value="${items.reason}">
                                             </div>

                                             <div class="row">
                                                      <a href="/bank/decline/${items.id}">Approval</a>
                                                      <input type="submit" value="Decline" class="decline-button">
                                             </div>
                                       </form>
                             `;
                             createAccountClosingContainer.classList.add('account-closing');

                             closingContainer.appendChild(createAccountClosingContainer);
                         }
    });
     accountholders.innerHTML =  count;
     bankBalance.innerHTML = "₹ " + totalBalance;
     totalCredit.innerHTML = "₹ " + totalCreditAmount;
     totalOutstanding.innerHTML = "₹ " + totalOutstandingAmount;

})

