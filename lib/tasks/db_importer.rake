require 'json'

namespace :db_importer do
  desc "TODO"
  task recipes: :environment do
    file = File.read('/home/antoroko/Desktop/Hackathons/junction/tarjous/recipes-query-kasvis.json')
    prod_hash = JSON.parse file
    prod_hash.each do |prod|
      @recipe = Recipe.new
      prod.each do |key, value|
        if key == 'Id' then
          @recipe.code = value
        else
          @recipe[key.underscore]=value.to_s
        end
        @recipe.save
      end
    end
  end

  task products: :environment do
    file = File.read('/home/antoroko/Desktop/Hackathons/junction/tarjous/grocery-products-query-kasvis.json')
    prod_hash = JSON.parse file
    prod_hash.each do |prod|
      @product = Product.new
      prod.each do |key, value|
        if key == 'labelName' then
          @product.label_name = value['english']
        elsif key == 'marketingName' then
          @product.marketing_name = value['english']
        elsif key == 'pictureUrls' then
          @product.picture_urls = value[0]['original'] if value.present?
        elsif key == 'prod_attributes' then
          @product.prod_attributes = value.to_json if value.present?
        else
          @product[key.underscore]=value.to_s if key.present?
        end
        @product.save
      end
    end
  end

end
