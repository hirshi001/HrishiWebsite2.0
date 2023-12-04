let table = null;
let copy = null;
const onUserLoad = () => {
    // Create an in memory copy of the users table with id as key
    table = document.getElementById("user-table")
    copy = document.getElementById("copy-table")

    // add event listeners to the table to listen for changes
    table.addEventListener("input", onInput);
    // add event listeners for all the Reset buttons
    let resetButtons = document.getElementsByClassName("reset-button");
    for (let i = 0; i < resetButtons.length; i++) {
        resetButtons[i].addEventListener("click", onReset);
    }

}

const onReset = (e) => {
    // get the row and column of the changed cell
    let row = e.target.parentNode.parentNode;
    let column = e.target.parentNode;

    // get the original value of the cell
    let originalRow = copy.rows[row.rowIndex];

    // reset
    row.innerHTML = originalRow.innerHTML;
}

const onInput = (e) => {
    // get the row and column of the changed cell
    let row = e.target.parentNode.parentNode;
    let column = e.target.parentNode;


    let value = getValue(e.target)

    // get the original value of the cell
    let originalValue = getValue(copy.rows[row.rowIndex].cells[column.cellIndex].getElementsByTagName("input")[0]);

    let button = table.rows[row.rowIndex].getElementsByClassName("reset-button")[0]
    if (value !== originalValue) {
        button.disabled = false;
    }
}

const getValue = (e) => {
    // console.log(e)
    if (e.type === "checkbox") {
        return e.checked;
    }
    return e.value;
}

const getTableValue = (row, column) => {
    let cell = table.rows[row].cells[column];
    if(cell === undefined) {
        return null;
    }
    if(cell.innerText !== undefined && cell.innerText !== "" && cell.childNodes.length === 1) {
        return cell.innerText;
    }
    // check if cell has input
    let input = cell.getElementsByTagName("input")[0];
    if(input !== undefined) {
        return getValue(input);
    }
}
const save = () => {
    let users = [];
    for (let i = 1; i < table.rows.length; i++) {
        // check if reset button is disabled
        if (table.rows[i].getElementsByClassName("reset-button")[0].disabled) {
            continue;
        }

        let row = table.rows[i];
        let user = {};
        for(let j = 1; j < row.cells.length; j++) {
            let cell = row.cells[j];
            let value = getTableValue(i, j);
            let key = table.rows[0].cells[j].innerText;
            user[key] = value;
        }
        users.push(user);
    }
    console.log(users)
    fetch("/admin/editusers/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(users)
    }).then(response => response.text())
        .then(data => {
            alert(data);
            window.location.reload();
        })


}
