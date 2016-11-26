class ChangeAttributes < ActiveRecord::Migration[5.0]
  def change
    rename_column :products, :attributes, :prod_attributes
  end
end
