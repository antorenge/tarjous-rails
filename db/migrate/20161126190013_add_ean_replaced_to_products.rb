class AddEanReplacedToProducts < ActiveRecord::Migration[5.0]
  def change
    add_column :products, :ean_replaced, :string
  end
end
