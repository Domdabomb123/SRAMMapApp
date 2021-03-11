package dominicschumerth.c.srammapapp.listener

interface AdapterOnClickItemListener<M> {
    fun onItemClicked(position: Int, item: M)
}