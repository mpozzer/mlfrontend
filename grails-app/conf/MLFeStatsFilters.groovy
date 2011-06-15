

class MLFeStatsFilters {

    def filters = {
        all(uri:'/**') {
            before = {
				request.setAttribute('com.mercadolibre.frontend.StartRequest',System.currentTimeMillis())
            }
        }
    }
    
}
