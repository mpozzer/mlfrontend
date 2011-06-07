package com.mercadolibre.frontend

class StatsFilters {

    def filters = {
        all(uri:'/**') {
            before = {
				request.setAttribute('com.mercadolibre.frontend.StartRequest',System.currentTimeMillis())
            }
        }
    }
    
}
