class AddIngredientTypeToProducts < ActiveRecord::Migration[5.0]
  def change
    add_column :products, :ingredient_type, :string
  end
end
