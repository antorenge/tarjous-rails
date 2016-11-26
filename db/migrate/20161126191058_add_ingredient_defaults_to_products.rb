class AddIngredientDefaultsToProducts < ActiveRecord::Migration[5.0]
  def change
    add_column :products, :ingredient_defaults, :string
  end
end
