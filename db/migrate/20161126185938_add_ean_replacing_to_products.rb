class AddEanReplacingToProducts < ActiveRecord::Migration[5.0]
  def change
    add_column :products, :ean_replacing, :string
  end
end
