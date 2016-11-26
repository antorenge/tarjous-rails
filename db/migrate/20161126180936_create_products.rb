class CreateProducts < ActiveRecord::Migration[5.0]
  def change
    create_table :products do |t|
      t.string :ean
      t.string :attributes
      t.string :resource_type
      t.string :label_name
      t.string :marketing_name
      t.string :description
      t.string :category
      t.string :subcategory
      t.string :pricing_unit
      t.string :picture_urls

      t.timestamps
    end
  end
end
