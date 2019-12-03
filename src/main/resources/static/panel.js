function changeCurrent(registerId){
		$.ajax({
			url: '/change-current/',
			type: 'GET',
			data: { registerId: registerId},
            contentType: 'application/json; charset=utf-8',
            success: window.location.href = '/'
		})
}

function toggleSelection(buttonId){
		$.ajax({
            url: '/toggle-selection/',
            type: 'GET',
            data: { buttonId: buttonId},
            contentType: 'application/json; charset=utf-8',
            success: window.location.href = '/'
        })
}

function removeFromSelection(buttonId){
		$.ajax({
            url: '/remove-from-selection/',
            type: 'GET',
            data: { buttonId: buttonId},
            contentType: 'application/json; charset=utf-8',
            success: window.location.href = '/'
        })
}

function update(buttonId){
		$.ajax({
            url: '/update/',
            type: 'GET',
            data: { buttonId: buttonId},
            contentType: 'application/json; charset=utf-8',
            success: window.location.href = '/'
        })
}