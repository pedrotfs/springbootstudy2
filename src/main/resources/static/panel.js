function changeCurrent(registerId){
		$.ajax({
			url: '/change-current/',
			type: 'GET',
			data: { registerId: registerId},
            contentType: 'application/json; charset=utf-8',
            success: window.location.href = '/'
		});
		location.reload(true);
}

function toggleSelection(buttonId){
		$.ajax({
            url: '/toggle-selection/',
            type: 'GET',
            data: { buttonId: buttonId},
            contentType: 'application/json; charset=utf-8',
            success: window.location.href = '/'
        })
        location.reload(true);
}

function clearSelection(buttonId){
		$.ajax({
            url: '/clear-selection/',
            type: 'GET',
            data: { buttonId: buttonId},
            contentType: 'application/json; charset=utf-8',
            success: window.location.href = '/'
        })
        location.reload(true);
}

function removeFromSelection(buttonId){
		$.ajax({
            url: '/remove-from-selection/',
            type: 'GET',
            data: { buttonId: buttonId},
            contentType: 'application/json; charset=utf-8',
            success: window.location.href = '/'
        })
        location.reload(true);
}

function update(buttonId){
		$.ajax({
            url: '/update/',
            type: 'GET',
            data: { buttonId: buttonId},
            contentType: 'application/json; charset=utf-8',
            success: window.location.href = '/'
        })
        location.reload(true);
}

function check(value){
		$.ajax({
            url: '/check/',
            type: 'GET',
            data: { drawId: value},
            contentType: 'application/json; charset=utf-8',
            success: window.location.href = '/'
        })
        location.reload(true);
}