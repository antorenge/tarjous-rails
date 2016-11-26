class AddLatlongToProducts < ActiveRecord::Migration[5.0]
  def change
    add_column :products, :lat, :string
    add_column :products, :long, :string
  end
end
